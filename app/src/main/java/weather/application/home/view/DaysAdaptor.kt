package weather.application.home.view

class DaysAdaptor {
}
/*

class RecyclerAdapter (private val onClick:(Product) -> Unit) : ListAdapter<Product, ProductViewHolder>(ProductDiffUtil()) {
    lateinit var binding: DayItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        binding=DataBindingUtil.inflate(inflater,R.layout.day_item,parent,false)
        return ProductViewHolder(binding) }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentObj = getItem(position)
        binding.product=currentObj
        binding.imageProduct.setOnClickListener { onClick(currentObj)}
    }
}

class ProductViewHolder(binding: ProdcutItemBinding) : RecyclerView.ViewHolder(binding.root)

class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

 */